import { CommonConstants as Const } from '../common/constants/CommonConstants';

@Component
export struct HomeTabs {
  private scroller: Scroller = new Scroller();

  build() {

    Column() {
      Row() {
        Image($r('app.media.ic_mine'))
          .width("10%")
          .height("10%")
          .objectFit(ImageFit.Contain)
          .margin(4)
        Search().height("6%").margin({left:4,right:'12%'})
      }.align(Alignment.TopStart)

      Scroll(this.scroller) {
        Column() {
          Text("1")
          Text("2")
        }
      }
    }.align(Alignment.TopStart).width('100%').height('100%')
  }
}