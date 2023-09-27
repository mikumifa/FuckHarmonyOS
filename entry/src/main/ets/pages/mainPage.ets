import { CommonConstants as Const } from '../common/constants/CommonConstants';
import { BottomTabs } from '../view/BottomTabsComponent';
import { FindTabs } from '../view/FindTabsComponent';
import { HomeTabs } from '../view/HomeTabsComponent';
import { MineTabs } from '../view/MineTabsComponent';

@Entry
@Component
struct Index {
  @State @Watch('onIndexChange') bottomTabIndex: number = 0;
  private controller: TabsController = new TabsController();

  onIndexChange() {
    this.controller.changeIndex(this.bottomTabIndex);
  }

  build() {
    Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.End, justifyContent: FlexAlign.End }) {
      Tabs({ barPosition: BarPosition.End, index: 0, controller: this.controller }) {
        TabContent() {
          HomeTabs()
        }.padding({
          left: 12,
          right: 12
        })

        TabContent() {
          FindTabs()
        }.padding({
          left: 12,
          right: 12
        })

        TabContent() {
          MineTabs()
        }.padding({
          left: 12,
          right: 12
        })
      }.onChange((index: number) => {
        this.bottomTabIndex = index;
      }).width(Const.FULL_SIZE)
      .vertical(false)
      .barHeight(0)

      BottomTabs({ bottomTabIndex: $bottomTabIndex })
    }
    .width(Const.FULL_SIZE)
    .backgroundColor($r('app.color.background_color'))

  }
}

