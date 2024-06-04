'use strict';

const usernameSpan = document.querySelector('.usernameSpan'); //Form
const messageForm = document.querySelector('#messageForm'); //Form
const messageInput = document.querySelector('#message'); //Textfield
const chatArea = document.querySelector('#chat-messages');

let stompClient = null;
let currentUserID = null;
let selectedUserId = null;

window.onload = function() {
    currentUserID=usernameSpan.getAttribute("id");
    connect({ preventDefault: () => {} });
};

function connect(event) {
    //In the conf file the endPoint is /ws
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    //Stop the default run
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${currentUserID}/queue/messages`, onMessageReceived);
    // stompClient.subscribe(`/user/public`, onMessageReceived);

    // register the connected user
    findAndDisplayUsers().then();
}

function onError() {
    console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
}

async function findAndDisplayUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    const connectedUsersList = document.getElementById('users');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.userID;

    const userImage = document.createElement('img');
    userImage.src = '/images/blank-pfp.svg';
    userImage.alt = user.username;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.username;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    //Set the user as active
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';


    select = true;

    if (innerWidth < 768) {
        document.querySelector(".users-list").classList.add("hidden");
    }
    
    document.querySelector(".chat-area-parent").classList.remove("hidden");
    document.querySelector("#chat-messages").classList.remove("hidden");

    document.querySelector(".chat-area-parent").scrollTo(0, 1000);
    console.log(document.querySelector(".chat-area-parent").scrollHeight);
}

function displayMessage(senderID, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderID == currentUserID) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${currentUserID}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderID, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessageDto = {
            senderID: currentUserID,
            recipientID: selectedUserId,
            content: messageInput.value.trim(),
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessageDto));
        displayMessage(currentUserID, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}

async function onMessageReceived(payload) {
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    // selectedUserId = clickedUser.getAttribute('id');
    if (selectedUserId && selectedUserId == message.senderID) {
        displayMessage(message.senderID, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
    // if (selectedUserId) {
    //     document.querySelector(`#${selectedUserID}`).classList.add('active');
    // } else {
    //     messageForm.classList.add('hidden');
    // }
    const senderID = CSS.escape(message.senderID); // Escapes any special characters
    const notifiedUser = document.querySelector(`#${senderID}`);
    // const notifiedUser = document.querySelector(`#${message.senderID}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

messageForm.addEventListener('submit', sendMessage, true);
window.onbeforeunload = () => onLogout();







// Código enseñar conversación

let select = false;

document.querySelectorAll(".chat").forEach(chat => {
    chat.addEventListener("click", () => {
        select = true;

        if (innerWidth < 768) {
            document.querySelector(".chat-container").classList.add("hidden");
        }
        else {
            [...document.querySelectorAll(".selected")].map(chat => chat.classList.remove("selected"));
            chat.classList.add("selected");    
        }
        
        document.querySelector(".empty").classList.remove("d-md-flex");    
        document.querySelector(".empty").classList.add("hidden");
        document.querySelector("#chat-messages").classList.remove("hidden");

        document.querySelector("#chat-messages").scrollTo(0, document.querySelector("#chat-messages").scrollHeight);

    });
});

addEventListener("resize", () => {
    if (innerWidth < 768 && select) {
        document.querySelector("#chat-messages").classList.remove("hidden");
        document.querySelector(".users-list").classList.add("hidden");
    }
    else {
        document.querySelector(".chat-container").classList.remove("hidden");
        document.querySelector(".users-list").classList.remove("hidden");      
    }
});

// document.querySelector(".fa-paper-plane").addEventListener("click", () => document.querySelector("form").submit());