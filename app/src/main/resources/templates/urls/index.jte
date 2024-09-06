@import java.text.SimpleDateFormat
@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.UrlsPage;

@param UrlsPage page

@template.layout.page(
page = page,
    content = @`
    <body>
    <section>
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                            <td>
                            ${page.getLastChecks().get((long) url.getId()) == null ? "" :
                                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(page.getLastChecks().get(url.getId()).getCreatedAt())}
                            </td>
                            <td>
                            ${page.getLastChecks().get((long) url.getId()) == null ? "" :
                                String.valueOf(page.getLastChecks().get(url.getId()).getStatusCode())}
                            </td>
                        </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>
    </body>
    `
)