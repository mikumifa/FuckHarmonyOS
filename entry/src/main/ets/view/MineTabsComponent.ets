import { CommonConstants as Const } from '../common/constants/CommonConstants';

@Component
export struct MineTabs {
  private scroller: Scroller = new Scroller();

  build() {
    Text('mine')
      .lineHeight(25).fontSize(12).border({ width: 1 }).padding(10)
  }
}