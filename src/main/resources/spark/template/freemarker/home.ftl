<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="5">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <a href="/">My Home</a>
      <#if signedIn>
        <a href="/signout">Sign Out ${currentPlayer.name}</a>
      <#else>
        <a href="/signin">Sign In</a>
      </#if>
    </div>
    
    <div class="body">
      <p>Welcome to the world of online Checkers</p>
      <h2>Player Lobby</h2>
         <p>
           <#if signedIn>
              <#if message??>
                    <div class="message ${messageType}">${message}</div>
              </#if>
              <form action="./" method="POST"><button type="submit" name="computer" value="true">Want to Play a Game against a Computer</button></form>
              <br></br>
              <form action="./" method="POST"><button type="submit" name="replay" value="true">Want to Replay Last Game?</button></form>
              <br></br>
              <#list gameLobby as i>
                <form action="./" method="POST"><button type="submit" name="index" value="${i}">Ready to Play a Game with ${i}</button></form>
              </#list>
           <#else>
              ${gameLobby}
            </#if>
           <br></br>
         </p>
    </div>
    
  </div>
</body>
</html>
