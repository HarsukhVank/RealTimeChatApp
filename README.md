Real Time Chat App covers below points : 

Chat Interface:
● Create a single-screen chat app.
● Display a list of chatbot conversations.
● Each chat entry should show a preview of the latest message.
● Chatbot conversations will be cleared on app close.
Real-Time Syncing (P0):
● Implement socket-based communication for real-time updates. (use pie host for same)
● Updates should reflect immediately on the chat screen without refreshing.
Offline Functionality (P0):
● Queue messages that fail to send (simulated using a flag).
● Automatically retry sending queued messages when the device is back online.
Error Handling:
● Show clear alerts for API or network failures.
● Handle empty states:
○ No chats available
○ No internet connection
Chat Preview & Navigation (P1, P2):
● Show unread message previews for each chat (P1).
● (Optional) Ability to switch between individual chat views (P2).
