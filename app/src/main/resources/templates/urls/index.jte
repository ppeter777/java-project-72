@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.UrlsPage;
@param UrlsPage page

@template.layout.page(
    content = @`
    <div>
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
        <th>ID</th>
        <th>Имя</th>
        <th>Последняя проверка</th>
        <th>Код ответа</th>
      </tr>
        @for(var url : page.getUrls())
        <tr>
            <td>${url.getId()}</td>
            <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
            <td>
                @if(url.getChecked_at() == null)
                @else
                    ${url.getChecked_at().toString()}
                @endif
            </td>
            <td>
                @if(url.getResponseCode() == null)
                @else
                    ${url.getResponseCode().toString()}
                @endif
            </td>
        </tr>
        @endfor
    </table>
    </div>
    `,
            header = @`
                <p>
                <p>Анализатор страниц</p>
                <a href="${NamedRoutes.mainPagePath()}">Главная</a>
                <a href="${NamedRoutes.urlsPath()}">Сайты</a>
                </p>
            `
)