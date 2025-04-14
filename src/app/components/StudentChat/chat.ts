import { ChangeDetectorRef, Component, NgZone, ViewChild } from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';
import { ModalComponent } from 'angular-custom-modal';
import { NgScrollbar } from 'ngx-scrollbar';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { ChatService } from 'src/app/service/chat/chat.service';

@Component({
    moduleId: module.id,
    templateUrl: './chat.html',
    animations: [
        trigger('toggleAnimation', [
            transition(':enter', [style({ opacity: 0, transform: 'scale(0.95)' }), animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))]),
            transition(':leave', [animate('75ms', style({ opacity: 0, transform: 'scale(0.95)' }))]),
        ]),
    ],
})
export class ChatComponent {
    
    contactList: any[]=[] ;
    constructor(private ngZone: NgZone,public storeData: Store<any>, private dataService:ChatService,private cdref: ChangeDetectorRef) {
       this.initStore();
    }
    store: any;
    async initStore() {
        this.storeData
            .select((d) => d.index)
            .subscribe((d) => {
                this.store = d;
            });

            this.loginUser.id = Math.floor(Math.random() * 2) + 1;
            this.loginUser.name="My Id:" + this.loginUser.id ;
        
       
      
            this.dataService.getAllChat(this.loginUser.id).subscribe(response => {
                this.contactList = response.map((chatUser: any) => {
                  const mappedChatUser: any = {
                    userId: chatUser.id,
                    name: chatUser.firstname,
                    path: "profile-34.jpeg",
                    time: "",
                    messages: chatUser.messages .filter((message: any) => {
                        // Check if the condition is met
                        return message.toUserId == this.loginUser.id || message.fromUserId == this.loginUser.id;
                    })
                    .map((message: any) => {
                        const fromUserIdInt = parseInt(message.fromUserId);
                        const toUserIdInt = parseInt(message.toUserId);
                    
                                       // Check if the condition is met
                      if (message.toUserId == this.loginUser.id) {
                        console.log("1");
                        return {
                          id: message.id,
                          fromUserId: fromUserIdInt,
                          toUserId: toUserIdInt,
                          text: message.text,
                          time: this.formatTime( message.time)
                        };
                      } else if (message.fromUserId == this.loginUser.id) {
                        console.log("2");
                        // Return a default message if the condition is not met
                        return {
                            id: message.id,
                            fromUserId: fromUserIdInt,
                            toUserId: toUserIdInt,
                            text: message.text,
                            time:this.formatTime( message.time)
                          };
                      }
                      else                            console.log("3");

                      return {

                        id: message.id,
                        fromUserId: toUserIdInt,
                        toUserId: fromUserIdInt,
                        text: message.text,
                        time: this.formatTime( message.time)
                      };
                    })
                  };
                  if (mappedChatUser.messages.length > 0) {
                    mappedChatUser.time = mappedChatUser.messages[mappedChatUser.messages.length - 1].time;
                }
                  return mappedChatUser;
                });
                this.contactList.sort((a, b) => {
                    const timeA = new Date(a.time).getTime(); // Convert time to milliseconds
                    const timeB = new Date(b.time).getTime(); // Convert time to milliseconds
                    return timeB - timeA; // Sort in descending order (latest first)
                });
                
                const foundUser = this.contactList.find(user => user.userId === this.selectedUser.userId);

                // If the user is found, set selectedUser to the found user
                if (foundUser) {
                    this.selectedUser = foundUser;
                } else {
                    // Handle case where user is not found
                    console.log('User not found in contact list');
                }
              });
         
        }
        ngOnDestroy() {console.log("but");
            clearInterval(this.pollingTimer); // Clear the setInterval timer when the component is destroyed
          }
          pollingTimer: any; // Variable to store the setInterval timer

                  ngAfterViewInit(){ this.pollingTimer=setInterval(() => {
                    
                    this.dataService.getAllChat(this.loginUser.id).subscribe(response => {
                    this.contactList = response.map((chatUser: any) => {
                      const mappedChatUser: any = {
                        userId: chatUser.id,
                        name: chatUser.firstname,
                        path: "profile-34.jpeg",
                        time: "",
                        messages: chatUser.messages .filter((message: any) => {
                            // Check if the condition is met
                            return message.toUserId == this.loginUser.id || message.fromUserId == this.loginUser.id;
                        })
                        .map((message: any) => {
                            const fromUserIdInt = parseInt(message.fromUserId);
                            const toUserIdInt = parseInt(message.toUserId);
                        
                                           // Check if the condition is met
                          if (message.toUserId == this.loginUser.id) {
                            console.log("1");
                            return {
                              id: message.id,
                              fromUserId: fromUserIdInt,
                              toUserId: toUserIdInt,
                              text: message.text,
                              time: this.formatTime( message.time)
                            };
                          } else if (message.fromUserId == this.loginUser.id) {
                            console.log("2");
                            // Return a default message if the condition is not met
                            return {
                                id: message.id,
                                fromUserId: fromUserIdInt,
                                toUserId: toUserIdInt,
                                text: message.text,
                                time:this.formatTime( message.time)
                              };
                          }
                          else                            console.log("3");
    
                          return {
    
                            id: message.id,
                            fromUserId: toUserIdInt,
                            toUserId: fromUserIdInt,
                            text: message.text,
                            time: this.formatTime( message.time)
                          };
                        })
                      };
                      if (mappedChatUser.messages.length > 0) {
                        mappedChatUser.time = mappedChatUser.messages[mappedChatUser.messages.length - 1].time;
                    }
                      return mappedChatUser;
                    });
                   
                    this.contactList.sort((a, b) => {
                        const timeA = new Date(a.time).getTime(); // Convert time to milliseconds
                        const timeB = new Date(b.time).getTime(); // Convert time to milliseconds
                        return timeB - timeA; // Sort in descending order (latest first)
                    });
                    
                    const foundUser = this.contactList.find(user => user.userId === this.selectedUser.userId);
    
                    // If the user is found, set selectedUser to the found user
                    if (foundUser) {
                        this.selectedUser = foundUser;
                    } else {
                        // Handle case where user is not found
                        console.log('User not found in contact list');
                    }
                  });
                },3000);
                }

                /*
        connectToSSE(id: number): Observable<any> {
            return new Observable<any>(observer => {
              const eventSource = new EventSource(`http://localhost:8089/internshipApp/chats/getall/${id}`);
        
              eventSource.onmessage = event => {
                observer.next(JSON.parse(event.data));
              };
        npm install rxjs

              eventSource.onerror = error => {
                console.error('SSE error:', error);
                observer.error(error);
              };
        
              return () => eventSource.close();
            });
          
        }
        
    assignValue(id:any) { setInterval(() => {this.userid=id;
       // console.log(id);
      //  console.log(this.userid);
        this.dataService.getAllChat(this.loginUser.id).subscribe(response => {
        this.contactList = response.map((chatUser: any) => {
          const mappedChatUser: any = {
            userId: chatUser.userId,
            name: chatUser.name,
            path: "profile-34.jpeg",
            time: chatUser.time,
            preview: chatUser.preview,
            messages: chatUser.messages .filter((message: any) => {
                // Check if the condition is met
                return message.toUserId === this.loginUser.id || message.fromUserId === this.loginUser.id;
            })
            .map((message: any) => {

                               // Check if the condition is met
              if (message.toUserId === this.loginUser.id) {
                console.log("1");
                return {
                  id: message.id,
                  fromUserId: message.fromUserId,
                  toUserId: message.toUserId,
                  text: message.text,
                  time: message.time
                };
              } else if (message.fromUserId === this.loginUser.id) {
                console.log("2");
                // Return a default message if the condition is not met
                return {
                    id: message.id,
                    fromUserId: message.fromUserId,
                    toUserId: message.toUserId,
                    text: message.text,
                    time: message.time
                  };
              }
              else                            console.log("3");

              return {

                id: message.id,
                fromUserId: message.toUserId,
                toUserId: message.fromUserId,
                text: message.text,
                time: message.time
              };
            })
          };
          
          return mappedChatUser;
        });
        console.log(this.contactList);
      });
      
      
    },1000);
    this.cdref.detectChanges();

        
      
        

}*/
    @ViewChild('isAddNoteModal') isAddNoteModal!: ModalComponent;
    @ViewChild('isDeleteNoteModal') isDeleteNoteModal!: ModalComponent;
    @ViewChild('isViewNoteModal') isViewNoteModal!: ModalComponent;
    @ViewChild('scrollable') scrollable!: NgScrollbar;
    isShowUserChat = false;
    isShowChatMenu = false;
    loginUser = {
        id: 1,
        name: 'Man',
        path: 'profile-34.jpeg',
        designation: 'Software Developer',
    };

    
    searchUser = '';
    textMessage = '';
    selectedUser: any = null;

    searchUsers() {
        return this.contactList.filter((d: { name: string }) => {
            return d.name.toLowerCase().includes(this.searchUser);
        });
    }

    selectUser(user: any) {
        this.selectedUser = user;
        this.isShowUserChat = true;
        this.scrollToBottom();
       this.isShowChatMenu = false;
       console.log(this.selectedUser);

      /* setInterval(() => {  
        this.selectedUser = user;
      console.log("wassup");
    },2000);*/
    }

    sendMessage(otheruserid:any) {
        console.log(this.textMessage);
        if (this.textMessage.trim()) {
            
            let Message: any  = {
            fromUserId:otheruserid, // Assuming loginUser contains the current user's ID
            toUserId:  this.loginUser.id, // Set the appropriate value if needed
            text: this.textMessage, // Set the message text
            time: new Date().toISOString() // Set the current time in the correct format
          };

            this.dataService.addMessage(Message,otheruserid,this.loginUser.id)
            .subscribe(response => {
              // Handle the response if needed
              console.log('Message sent successfully:', response);
            }, error => {
              // Handle errors if the request fails
              console.error('Error sending message:', error);
            });

/////
            const user: any = this.contactList.find((d: { userId: any }) => d.userId === this.selectedUser.userId);
            user.messages.push({
                fromUserId: this.selectedUser.userId,
                toUserId: this.loginUser.id,
                text: this.textMessage,
                time: 'Just now',
            });
            this.textMessage = '';
            this.scrollToBottom();

          
        }

    }

    scrollToBottom() {
        if (this.isShowUserChat) {
            setTimeout(() => {
                this.scrollable.scrollTo({ bottom: 0 });
            });
        }
    }
    formatTime(timestamp: number): string {
        const months = [
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
        ];
        
        const date = new Date(timestamp);
        const month = months[date.getMonth()];
        const day = date.getDate();
        const hours = date.getHours();
        const minutes = date.getMinutes();
    
        // Add leading zero if necessary
        const formattedDay = day < 10 ? '0' + day : day.toString();
        const formattedHours = hours < 10 ? '0' + hours : hours.toString();
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes.toString();
    
        // Format the time as "Month Day Hour:Minute"
        return month + ' ' + formattedDay + ' ' + formattedHours + ':' + formattedMinutes;
    }
    
    
/*= [
        {
            userId: 1,
            name: 'Nia Hillyer',
            path: 'profile-16.jpeg',
            time: '2:09 PM',
            preview: 'How do you do?',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 1,
                    text: 'Hi, I am back from vacation',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 1,
                    text: 'How are you?',
                    time: '',
                },
                {
                    fromUserId: 1,
                    toUserId: 0,
                    text: 'Welcom Back',
                    time: '',
                },
                {
                    fromUserId: 1,
                    toUserId: 0,
                    text: 'I am all well',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 1,
                    text: 'Coffee?',
                    time: '',
                },
            ],
            active: true,
        },
        {
            userId: 2,
            name: 'Sean Freeman',
            path: 'profile-1.jpeg',
            time: '12:09 PM',
            preview: 'I was wondering...',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 2,
                    text: 'Hello',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 2,
                    text: "It's me",
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 2,
                    text: 'I have a question regarding project.',
                    time: '',
                },
            ],
            active: false,
        },
        {
            userId: 3,
            name: 'Alma Clarke',
            path: 'profile-2.jpeg',
            time: '1:44 PM',
            preview: 'I’ve forgotten how it felt before',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 3,
                    text: 'Hey Buddy.',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 3,
                    text: "What's up",
                    time: '',
                },
                {
                    fromUserId: 3,
                    toUserId: 0,
                    text: 'I am sick',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 3,
                    text: 'Not comming to office today.',
                    time: '',
                },
            ],
            active: true,
        },
        {
            userId: 4,
            name: 'Alan Green',
            path: 'profile-3.jpeg',
            time: '2:06 PM',
            preview: 'But we’re probably gonna need a new carpet.',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 4,
                    text: 'Hi, collect your check',
                    time: '',
                },
                {
                    fromUserId: 4,
                    toUserId: 0,
                    text: 'Ok, I will be there in 10 mins',
                    time: '',
                },
            ],
            active: true,
        },
        {
            userId: 5,
            name: 'Shaun Park',
            path: 'profile-4.jpeg',
            time: '2:05 PM',
            preview: 'It’s not that bad...',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 3,
                    text: 'Hi, I am back from vacation',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 3,
                    text: 'How are you?',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 5,
                    text: 'Welcom Back',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 5,
                    text: 'I am all well',
                    time: '',
                },
                {
                    fromUserId: 5,
                    toUserId: 0,
                    text: 'Coffee?',
                    time: '',
                },
            ],
            active: false,
        },
        {
            userId: 6,
            name: 'Roxanne',
            path: 'profile-5.jpeg',
            time: '2:00 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [
                {
                    fromUserId: 0,
                    toUserId: 6,
                    text: 'Hi',
                    time: '',
                },
                {
                    fromUserId: 0,
                    toUserId: 6,
                    text: 'Uploaded files to server.',
                    time: '',
                },
            ],
            active: false,
        },
        {
            userId: 7,
            name: 'Ernest Reeves',
            path: 'profile-6.jpeg',
            time: '2:09 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: true,
        },
        {
            userId: 8,
            name: 'Laurie Fox',
            path: 'profile-7.jpeg',
            time: '12:09 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: true,
        },
        {
            userId: 9,
            name: 'Xavier',
            path: 'profile-8.jpeg',
            time: '4:09 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: false,
        },
        {
            userId: 10,
            name: 'Susan Phillips',
            path: 'profile-9.jpeg',
            time: '9:00 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: true,
        },
        {
            userId: 11,
            name: 'Dale Butler',
            path: 'profile-10.jpeg',
            time: '5:09 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: false,
        },
        {
            userId: 12,
            name: 'Grace Roberts',
            path: 'user-profile.jpeg',
            time: '8:01 PM',
            preview: 'Wasup for the third time like is you bling bitch',
            messages: [],
            active: true,
        },
    ];*/
}
