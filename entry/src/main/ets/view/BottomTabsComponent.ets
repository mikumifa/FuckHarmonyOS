import { BottomTabsItem } from '../common/bean/BottomTabsItem';
import { BottomTabsList } from '../viewmodel/BottomTabsModel';
import { CommonConstants as Const } from '../common/constants/CommonConstants';


function getTabImgSrc(item: BottomTabsItem, index: number) {
  let imgSrc = item.icon;
  if (BottomTabsList.indexOf(item) === index) {
    imgSrc = item.iconSelected;
  }
  return imgSrc;
}

function getTabTextColor(item: BottomTabsItem, index: number) {
  let color = $r('app.color.bottom_tabs_font_color');
  if (BottomTabsList.indexOf(item) === index) {
    color = $r('app.color.bottom_tabs_font_color_selected');
  }
  return color;
}

@Component
export struct BottomTabs {
  @Link bottomTabIndex: number;

  build() {
    Flex({ direction: FlexDirection.Row, justifyContent: FlexAlign.SpaceEvenly, alignItems: ItemAlign.Center }) {
      ForEach(BottomTabsList, (item: BottomTabsItem) => {
        Column() {
          Image(getTabImgSrc(item, this.bottomTabIndex))
            .width(Const.BOTTOM_IMAGE_SIZE)
            .height(Const.BOTTOM_IMAGE_SIZE)
            .objectFit(ImageFit.Contain)
            .margin({ bottom:12 })

          Text(item.text)
            .fontSize(12)
            .opacity(Const.OPACITY)
            .fontColor(getTabTextColor(item, this.bottomTabIndex))
        }
        .width(Const.BOTTOM_TAB_WIDTH)
        .onClick(() => {
          this.bottomTabIndex = BottomTabsList.indexOf(item);
        })
      }, (item: BottomTabsItem) => JSON.stringify(item))
    }
    .width(Const.FULL_SIZE)
    .height(Const.BOTTOM_TAB_HEIGHT)
    .backgroundColor($r('app.color.bottom_tabs_background_color'))
  }
}