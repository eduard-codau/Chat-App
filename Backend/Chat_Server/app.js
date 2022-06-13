const express = require('express');
const app = express();
// import fetch from 'node-fetch';
const fetch = require('node-fetch');
const {Server} = require("socket.io");
const server = require('http').createServer(app);
const io = new Server(server,{
  cors: {
      origins: "*:*"
  }
});

var moment = require('moment');
app.use(express.static("public"));

server.listen(8000, () => {
  console.log("Server started on port 8000")
})

app.get("/", (req,res) => {
  res.sendFile(__dirname + "/public/index.html");
})

const getMoment = () => "[ " + moment().format('YYYY-MM-DD hh:mm:ss') + " ] - ";

const actives = {}

// contain socket id, friendId, and username
const allUsers = {}
// contain socket id - uid dict
const socketUserIdPairs = {}


const fields = ["chatId", "fromUserId", "text"] // se poate adauga si "filePath" in cazul trimiterii de fisiere

io.on('connection', (socket) => {
  console.log("New socket: " + socket.id);
  // mesajul de initializare va cuprinde: 
  //  - id-ul utilizatorului care incepe comunicatia 
  //  - id-ul partenerului de comunicatie
  //  - numele utilizatorului care initializeaza comunicatia
  //  - numele partenerului de comunicatie
  // client nou, va notifica ceilalti clienti de
  socket.on('init', data => {
    console.log("[ " + getMoment() + " ]" + "Socket: " + socket.id + "> set for user <" + data.username + "> and his friend <" + data.friendName + ">");
    actives[data.userId] = 0;
    allUsers[data.userId] = {
      "socketId": socket.id,
      "friendId": data.friendId,
      "username":data.username
    }

    socketUserIdPairs[socket.id] = data.userId;
  })

  socket.on('disconnect', () => {
    console.log("User disconnected: " + socket.id);
  })

  // socket.on('receive-message',data => {
  //   console.log("Message received: " + data);
  // })
  //trimite mesaj
  socket.on('send-message',data => {
    console.log("\n[ " + getMoment() + " ] Someone sent a message ");
    // validare mesaj
    let valid = true;
    for (let i = 0; i < fields.length; i++) {
        if (!data.hasOwnProperty(fields[i])) {
            valid = false;
            break;
        }
    }

    if (valid) {
      console.log(data);
      console.log("[ " + getMoment() + " ] "+ allUsers[socketUserIdPairs[socket.id]].username + " sent a valid message. ");
      // aici am putea salva mesajul in API-ul pentru mesaje cu baza de date
      fetch('http://localhost:8081/api/messages', {
        method: 'post',
        body: JSON.stringify({
          chatId:data.chatId,
          fromUser:data.fromUserId,
          text:data.text,
          sentAt:moment.now()
        }),
        headers: {'Content-Type': 'application/json'},
    }).then((resp)=>{console.log(resp)})
      // console.log(data);
      const userId = socketUserIdPairs[socket.id];
      const friendId = allUsers[userId].friendId;

      // daca prietenul a initializat o sesiune de comunicatie, ii trimitem o notificare 
      // din care sa rezulte primirea mesajului
      if (allUsers[friendId] !== undefined) {
          const socketFriendId = allUsers[friendId].socketId;
          data.send_moment = moment.now();
          data.username = allUsers[userId].username;
          // evenimentul 'receive-message' trebuie definit la client(angular)
          io.to(socketFriendId).emit("receive-message", data);
      }
      else
      {
          console.log("Prietenul nu s-a conectat");
      }
    
    }
    
  })
})

// evenimente 
