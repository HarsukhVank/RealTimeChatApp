Real Time Chat App covers below points : 

Chat Interface: <br/>
● Create a single-screen chat app. <br/>
● Display a list of chatbot conversations. <br/>
● Each chat entry should show a preview of the latest message.<br/>
● Chatbot conversations will be cleared on app close.<br/>

Real-Time Syncing (P0):<br/>
● Implement socket-based communication for real-time updates. (use pie host for same)<br/>
● Updates should reflect immediately on the chat screen without refreshing.<br/>

Offline Functionality (P0):<br/>
● Queue messages that fail to send (simulated using a flag).<br/>
● Automatically retry sending queued messages when the device is back online.<br/>

Error Handling:<br/>
● Show clear alerts for API or network failures.<br/>
● Handle empty states:<br/>
○ No chats available<br/>
○ No internet connection<br/>

Chat Preview & Navigation (P1, P2):<br/>
● Show unread message previews for each chat (P1).<br/>
● (Optional) Ability to switch between individual chat views (P2).<br/>
