@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.UrlPage;
@param UrlPage page

@template.layout.page(
content = @`
    <div>${page.getUrl().getId()}</div>
    <div>${page.getUrl().getName()}</div>
`
)