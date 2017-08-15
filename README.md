# buildAPKsTutorials
Android APK tutorial sources that build in [Termux](https://termux.com) on Android. This repository is a submodule for [buildAPKs.](https://github.com/sdrausty/buildAPKs)

To install it in its' proper place, copy and paste the following into [Termux.](https://termux.com)
```
cd && git clone https://github.com/sdrausty/buildAPKs
cd buildAPKs
git submodule init
git submodule update sources/tutorials
echo "Run the following command to build these APKs."
./sources/buildTutorials.sh
```
