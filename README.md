# GitHubRepositories

A simple one page app that gets the list of public repositories for a given organization and also shows the most active contributor for those repositories.

Due to rate limits, the app requires Github's personal access token to work which can be generated following the instructions [here]. 
Once you have the token, in order to make the app compile please add a `secrets.properties` file to the root of the project and add the token there.
It should looks something like this:

```
GITHUB_PERSONAL_TOKEN = XXXXXXXX
```

[here]: https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token
