@import java.text.SimpleDateFormat

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
        <table style="width:50%" border="1" align="center">
                <tr>
                    <td>ID</td>
                    <td>Имя</td>
                    <td>Дата создания</td>
                </tr>
                <tr>
                    <td>${page.getUrl().getId()}</td>
                    <td>${page.getUrl().getName()}</td>
                    <td>${new SimpleDateFormat("dd/MM/yyyy hh:mm").format(page.getUrl().getCreatedAt())}</td>
                </tr>
        </table>
        <p></p>
        <h2>Проверки</h2>
        <form action="${NamedRoutes.checkPath(page.getUrl().getId())}" method="get">
            <button>Проверить</button>
        </form>
        <p>
        </p>
            <table style="width:50%" border="1" align="center">
                <tr>
                    <td>ID</td>
                    <td>Код ответа</td>
                    <td>title</td>
                    <td>h1</td>
                    <td>description</td>
                    <td>Дата проверки</td>
                </tr>
                                @if(page.getUrlChecks().size() == 0)
                                @else
                                @for(var check : page.getUrlChecks())
                                <tr>
                                                        <td>${check.getId()}</td>
                                                        <td>${check.getStatusCode()}</td>
                                                        <td>${check.getTitle()}</td>
                                                        <td>${check.getH1()}</td>
                                                        <td>${check.getDescription()}</td>
                                                        <td>${new SimpleDateFormat("dd/MM/yyyy hh:mm").format(check.getCreatedAt())}</td>
                                </tr>
                                @endfor
                                @endif
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