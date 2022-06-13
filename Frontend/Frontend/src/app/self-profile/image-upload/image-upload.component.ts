import { HttpClient, HttpEventType  } from '@angular/common/http';
import { Component } from '@angular/core';
import { UserService } from 'src/app/shared/services/User/user.service';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})
export class ImageUploadComponent {

 constructor(private userService: UserService) { }

  selectedFile!: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string | undefined;
  imageName: any;

  //Gets called when the user selects an image
  public onFileChanged(event: any) {
    //Select File
    if(event.target != null)
    {
      this.selectedFile = event.target.files[0];
      this.onUpload();
    }
  }

  onUpload() {
    console.log(this.selectedFile);
    
    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    

    var userId = localStorage.getItem('userId');


    //Make a call to the Spring Boot Application to save the image
    this.userService.uploadProfileImage(Number(userId), uploadImageData).subscribe({
      next: receivedData => {
        window.location.reload();
      },
      error: error => {
          console.error('There was an error!', error);
      }
   })
  }
}
