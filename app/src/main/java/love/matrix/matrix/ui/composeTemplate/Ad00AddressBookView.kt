package love.matrix.matrix.ui.composeTemplate


/**
 * 2020/7/3
 *
 * isOpen控制【@Composable】内部某个组件的显示和隐藏
 *
 * 由于isOpen出现在很多地方，一一给它注释是不可行的，所以在这里“统一回复”
 * 目前isOpen的源头都在xxxScreen里，它的后面紧跟着 Scaffold()手脚架代码块
 * 以后可能会有更好控制组件显示和隐藏的方法，如果没有，要继续使用这种方法的话
 * 就给isOpen加后缀进行定位，比如isOpen01,isOpen20,等等，
 * Ad03XxxView(isOpen02: MutableState<Boolean>)
 * 就说明isOpen定义在Ad02这个枝干里
 *
 *
 * 2020/7/14
 *
 * 这种方式太不优雅，后续会使用koin和viewmodel,使用多activity模式，不固死在这片树林中
 *
 * 函数体内不应该放那么多参数，3个合适，5个为多，参数多了就应该想办法换种实现方法去做
 *
 */
/*
@Composable
fun Ad00AddressBookView(isOpen: MutableState<Boolean>) {

    Ad01Gallery.Content(isOpen,Ad01Gallery.Routing.AlbumList)
}
*/
