# TowerDefenseGame2D
prog2のreport6で作成した2Dのタワーディフェンスゲーム。

## ゲーム概要・プレイの仕方
　左端から出てくるモンスターが右端に到達したらゲームオーバー。
一定時間耐えきるかすべての敵を倒したらクリア。
ステージごとの予算でユニットを買い，配置する。<br>
　残り時間と予算は左上に，購入可能なユニットは右上にある。
ユニットを押すと，マウスカーソルにアイコンがくっついてくるので，配置したい場所で左クリックで配置。

## Gradlew使用法
実行可能なJarの生成：
```shell script
gradlew desktop:dist
```
JUnitテストの実行：
```shell script
gradlew core:test
```
