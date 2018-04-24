# WheelPicker
The project contains a customizable WheelPicker which is more flexible than android native NumberPicker widget. 
## Features
- Customizable font include text size, typeface, color and alignment.
- Customizable picker item count, can be odd or even number.
- Support both positive and negative values
- Support different picker style, like limited picker, rounded wrapped picker and unlimited picker.
- Allow users to set their adapters for supportting different requirements. The video below shows an implementation of date picker by pluging custom date adapter into the wheel picker.
### Attributes
|attribute name|attribute description|
|:-:|:-:|
|fadingEdgeEnabled|enable/disable the fading edge attribute.|
|max|The max index(value) of the picker, can be negative, default is Integer.MAX_VALUE.|
|min|The minimum index(value) of the picker, can be negative, default is Integer.MIN_VALUE|
|selectedTextColor|The text color of selected item.|
|textColor|The text color of unselected item.|
|textSize|Text size.|
|typeface|Text font typeface.|
|wheelItemCount|The visible item count of the picker.|
|wrapSelectorWheel|true if the picker is rounded wrapped, default is false.|
|align|The text alignment, can be LEFT, CENTER, RIGHT.|
## Demos
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_wrap.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_change_color.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_change_item_count.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_date_picker.gif)


