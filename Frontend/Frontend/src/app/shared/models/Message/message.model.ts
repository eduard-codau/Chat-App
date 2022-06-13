export class Message {

    messageId: Number = -1;
    chatId: Number = -1;
    fromUser: Number | null = -1;

    text:string = "";
    sentAt:string = "";

    username: string | null = "";

    constructor( text: string){
        this.text = text;
    }
}
