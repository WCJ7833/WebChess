<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers Sign-In</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">

    <h1>Web Checkers</h1>
    <div class="navigation">
          <a href="/">My Home</a>
    </div>

    <div class="body">

      <#if message??>
      <div class="message ${messageType}">${message}</div>
      </#if>

      <form action="./signin" method="POST">
      <p>Sign In to the world of online Checkers.</p>
      <input name = "UserName"/>
      <button type="submit">Ok</button>
      </form>
    </div>

  </div>
</body>
</html>
