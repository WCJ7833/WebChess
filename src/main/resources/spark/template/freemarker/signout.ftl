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
      <#if signedIn>
        <form action="./signout" method="POST">
        <p>Sign Out from the world of online Checkers?</p>
        <button type="submit">Ok</button>
      </form>
      <#else>
         <p>You have been signed out.</p>
      </#if>
    </div>

  </div>
</body>
</html>
