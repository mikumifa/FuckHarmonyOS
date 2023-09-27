import { CommonConstants as Const } from '../common/constants/CommonConstants';

@Component
export struct FindTabs {
  private scroller: Scroller = new Scroller();

  build() {
    Text('Find')
      .lineHeight(25).fontSize(12).border({ width: 1 }).padding(10)
  }
}