@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.UrlPage;
@param UrlPage page

@template.layout.page(
content = @`
    <div align="center">
        <h1>Сайт ${page.getUrl().getName()}</h1>
              <style type="text/css">
               TABLE {
                width: 300px;
                border-collapse: collapse;
               }
               TD, TH {
                padding: 3px;
                border: 1px solid black;
               }
               TH {
                background: #b0e0e6;
               }
              </style>
        <table style="width:50%" border="2" align="center">
                <tr>
                    <td>ID</td>
                    <td>Имя</td>
                    <td>Дата создания</td>
                </tr>
                <tr>
                    <td>${page.getUrl().getId()}</td>
                    <td>${page.getUrl().getName()}</td>
                    <td>${page.getUrl().getCreated_at().toString()}</td>
                </tr>
        </table>
`,
             header = @`
                 <p>
                 <p>Анализатор страниц</p>
                 <a href="${NamedRoutes.mainPagePath()}">Главная</a>
                 <a href="${NamedRoutes.urlsPath()}">Сайты</a>
                 </p>
             `

)