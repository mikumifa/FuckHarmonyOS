
import { BottomTabsItem } from '../common/bean/BottomTabsItem'

export const BottomTabsList: Array<BottomTabsItem> = [
  {
    icon: $r('app.media.ic_home'),
    iconSelected: $r('app.media.ic_home_selected'),
    text: $r('app.string.bottom_text_home')
  },
  {
    icon: $r('app.media.ic_find'),
    iconSelected: $r('app.media.ic_find_selected'),
    text: $r('app.string.bottom_text_find')
  },
  {
    icon: $r('app.media.ic_mine'),
    iconSelected: $r('app.media.ic_mine_selected'),
    text: $r('app.string.bottom_text_mine')
  }
]