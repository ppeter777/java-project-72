@import hexlet.code.dto.MainPage
@param MainPage page

@templates.layout.page(
    content = @`
        <h1 class="text-body-emphasis">Hi Hexlet!</h1>
        <p>Javalin + jte</p>
        @if(page.getUrl() != null)
          Welcome, ${page.getUrl()}. if you want to sign out remove cookie called JSESSIONID
        @endif
    `
)