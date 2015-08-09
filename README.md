# Play Together
Arrange participation to fosball matches with your colleagues!
### Cloning the repo
PlayTogether uses the [Rainbowlibs](https://github.com/rainbowbreeze/rainbowlibs) libraries, added as [submodules](http://git-scm.com/book/en/v2/Git-Tools-Submodules) to the repo, so using the following syntax to clone:
```sh
git clone --recursive https://github.com/rainbowbreeze/playtogether.git master
cd master
git submodule update --remote
```
As referent, I used this syntaxt to add the submodule:
```sh
rm -rf PlayTogether/ext-libraries/rainbowlibs
git submodule add https://github.com/rainbowbreeze/rainbowlibs.git PlayTogether/ext-libraries/rainbowlibs
```
[Subtree](https://developer.atlassian.com/blog/2015/05/the-power-of-git-subtree/) can also be used, but I prefer the submodules so I can easily push changes to them inside Android Studio

