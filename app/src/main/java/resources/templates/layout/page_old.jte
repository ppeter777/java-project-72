@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.MainPage
@import hexlet.code.dto.UrlsPage
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null
@param Content header

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Проект Анализатор страниц</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    </head>
    <body>
    <div class="header">
        ${header}

    </div>
    <div class="content">
        ${content}
                        @if(page != null && page.getFlash() != null)
                            <p>${page.getFlash()}</p>
                        @endif
    </div>
    </body>
</html>