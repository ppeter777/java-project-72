@import hexlet.code.utils.NamedRoutes

@template.layout.page(
    content = @`
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
    <body>
    <div class="col-lg-8 mx-auto p-4 py-md-5">
        <main>
            <div class="container">
              <div class="row">
                <div class="col">
                  <h1>Анализатор страниц</h1>
                  <p>Бесплатно проверяйте сайты на SEO пригодность</p>
                </div>
        <form action="${NamedRoutes.urlsPath()}" method="post">
            <lable>
                URL
                <input type="text" name="url">
            </lable>
            <input type="submit">
        </form>
              </div>
            </div>
        </main>
    </div>
    </body>
</html>
    `,
        header = @`
            <p>
            <p>Анализатор страниц</p>
            <a href="${NamedRoutes.mainPagePath()}">Главная</a>
            <a href="${NamedRoutes.urlsPath()}">Сайты</a>
            </p>
        `
)