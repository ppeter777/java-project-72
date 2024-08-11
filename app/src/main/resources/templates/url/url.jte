@import java.text.SimpleDateFormat

@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.UrlPage;
@param UrlPage page

@template.layout.page(
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайт ${page.getUrl().getName()}</h1>
                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                        <tr>
                            <td>ID</td>
                            <td>${page.getUrl().getId()}</td>
                        </tr>
                        <tr>
                            <td>Имя</td>
                            <td>${page.getUrl().getName()}</td>
                        </tr>
                        <tr>
                            <td>Дата создания</td>
                            <td>${new SimpleDateFormat("dd/MM/yyyy hh:mm").format(page.getUrl().getCreatedAt())}</td>
                        </tr>
                    </tbody>
                </table>
            <h2 class="mt-5">Проверки</h2>
                <form action="${NamedRoutes.checkPath(page.getUrl().getId())}" method="get">
                    <button type="submit" class="btn btn-primary">Запустить проверку</button>
                </form>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <tr><th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                    </tr>
                </thead>
                    <tbody>
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
                    </tbody>
            </table>
        </div>
    </section>
`,
             header = @`
                 <p>
                 <p>Анализатор страниц</p>
                 <a href="${NamedRoutes.mainPagePath()}">Главная</a>
                 <a href="${NamedRoutes.urlsPath()}">Сайты</a>
                 </p>
                 `)