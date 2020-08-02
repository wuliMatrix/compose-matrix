package love.matrix.matrix

/**
 * 2020/7/3
 *
 * 关于UI代码的整体结构
 *
 * 整体可以用一棵树来类比，MainActivity里的
 *  Crossfade(appState.currentScreen) { screen ->
 *       when (screen) {
 *           Screen.Matrix -> MatrixScreen()
 *           Screen.Shop -> ShopScreen()
 *           Screen.Video -> VideoScreen(videoCurrentScreen)
 *           Screen.Chat -> ChatScreen()
 *           Screen.Mine -> MineScreen()
 *           Screen.Photo -> PhotoScreen()
 *       }
 *  }
 *  就是主树干，向上不断延伸出新枝干，在新的一个类里
 *  1, enum类的每一个值，就是一个新的枝干芽
 *  enum class DrawerVideoScreen {Screen1,Screen2,Screen3,,,,}
 *  2，通过when让枝干芽长出新枝干
 *    when (currentScreen) {
 *         DrawerVideoScreen.Screen1 -> Screen1Component()
 *         DrawerVideoScreen.Screen2 -> Screen2Component()
 *         DrawerVideoScreen.Screen3 -> Screen3Component()
 *    }
 *  3，如果新枝干里带【State】状态值，说明这是一个有层次结构的枝干,当前类的所有enum值就会组成一条枝干。
 *    反过来说明就是看到一个UI类里带有 enum类（或sealed class类），就说明这个类长出了一条新枝干
 *   when (infoState.currentScreen) {
 *        InformationScreen.info -> LazyColumnItemsScrollableComponent(
 *               getPersonList(), infoState
 *        )
 *        InformationScreen.chat -> In02Chat(
 *              infoState
 *        )
 *   }
 *             &             *
 *           &             *            ￥
 *         &             *             ￥
 *      =====================================【枝干】
 *               %                #
 *                 %                 #
 *                   %
 *
 *  4，目前有两种方法让页面串连成【枝干】，一种是Router,一种是backPress
 *
 *     Router来源于github开源项目:
 *     https://github.com/zsoltk/compose-router
 *     backPress来源来源于github开源项目:
 *     https://github.com/vinaygaba/Learn-Jetpack-Compose-By-Example
 *
 *    Router的代码结构是这样的
 *    interface Ad01Gallery {
 *         sealed class Routing {
 *              object AlbumList : Routing()
 *              data class PhotosOfAlbum(val album: Album) : Routing()
 *              data class FullScreenPhoto(val photo: Photo) : Routing()
 *         }
 *
 *         companion object {
 *              @Composable
 *              fun Content(defaultRouting: Routing) {
 *                   Router(defaultRouting) { backStack ->
 *                        when (val routing = backStack.last()) {
 *                              is Routing.AlbumList -> AlbumList.Content(
 *                                        onAlbumSelected = {
 *                                              backStack.push(Routing.PhotosOfAlbum(it))
 *                                        })
 *                              is Routing.PhotosOfAlbum -> PhotosOfAlbum.Content(
 *  在Router里                                   album = routing.album,
 *  sealed class相当于enum                 onPhotoSelected = {
 *  backStack.push相当于State                     backStack.push(Routing.FullScreenPhoto(it))
 *                                       })
 *                              is Routing.FullScreenPhoto -> FullScreenPhoto.Content(
 *                                        photo = routing.photo
 *                                        )}
 *                        }
 *                   }
 *              }
 *        }
 *    }
 *
 *    backPress的代码结构是这样的
 *    class XxxState {
 *          var currentScreen by mutableStateOf(XxxScreen.info)
 *    }
 *    enum class XxxScreen {info,chat,,,,}
 *
 *    @Composable
 *    fun Xxx(aaaState: XxxState) {
 *         ....
 *         when (aaaState.currentScreen) {
 *               XxxScreen.info -> LazyColumnItemsScrollableComponent(
 *                    getPersonList(), aaaState
 *               )
 *               XxxScreen.chat -> Xx02Chat(
 *                    aaaState
 *               )
 *         }
 *     }
 *     ...
 *     @Composable
 *     fun Xx02Chat(aaaState: XxxState) {
 *         ....
 *         BackButtonHandler {
 *              aaaState.currentScreen = XxxScreen.info
 *         }
 *     }
 *
 *     从代码结构来看，Router是嵌套比较深的，层级分明;backPress嵌套比较浅，层级随意组合。
 *     从代码气质来看，backPress与compose组件的气质相吻合，所以本软件里尽量使用backPress进行页面导航
 *
 *     当然如果官方推出更好用的【NavigateTo】组件进行页面切换的话，就换成官方的【NavigateTo】组件
 *
 *
 *
 * 2020/7/8
 *
 * 做了几天无用功，不得不放弃使用backPress，因为用它传不了参数（能传参数，实现起来也没Router简洁）
 *
 * 努力过才明白，compose-router是多么的优秀
 *
 * compose-router与我这个软件所倡导的“脑图”结构还是很吻合的，
 *
 * 树结，
 *
 * @Composable
 * fun Matrix69Post(postsRepository: PostsRepository) { // 命名方式 xxxx69xxxx
 *
 *      Matrix69Post.Content(postsRepository, Matrix69Post.Routing.Post)
 * }
 *
 * 树干，
 *
 * interface Matrix69Post {
 *      sealed class Routing() {
 *           object Post : Routing()
 *           data class Article(val postId: String) : Routing() //所带的变量值是下一个视图所需要的
 *      }
 *
 *      companion object {
 *      @Composable
 *      fun Content(
 *           postsRepository: PostsRepository,
 *           defaultRouting: Routing
 *      ) {
 *           Router(defaultRouting) { backStack ->
 *                 when (val routing = backStack.last()) { //routing也很巧妙，视图间通过它实现了传值
 *                       is Routing.Post -> Po00Post.Content(
 *
 *                             //数据流。数据一般在activity里产生的，也可以说是上个枝干产生的，流到了这个页面上
 *                              postsRepository = postsRepository,
 *                              onPostSelected = {
 *                                   backStack.push(Routing.Article(it))//前进
 *                              })
 *
 *  is很巧妙，不能少  ->    is Routing.Article -> Po01Article.Content(
 *                              postId = routing.postId,//传值 (视图中产生的值传到下一个视图）特定时刻产生
 *                              postsRepository = postsRepository,
 *                              onBack = {
 *                                    backStack.pop()//后退
 *                              }
 *                        )
 *                   }
 *               }
 *           }
 *      }
 *  }
 *
 *
 * 树叶，
 *
 *  interface Po01Article { //树干与叶子连结的地方 ，叶结  命名方式  xxxView
 *
 *       companion object { //companion只括中这一个@Composable即可，所以嵌套深的问题就轻易解决了
 *            @Composable
 *            fun Content(postId: String, postsRepository: PostsRepository, onBack: () -> Unit) {
 *                 Po01ArticleView(postId,postsRepository,onBack)
 *           }
 *      }
 *  }
 *
 *  @Composable
 *  fun Po01ArticleView( //叶子   @Composable不被嵌套，清清爽爽
 *      postId: String,
 *      postsRepository: PostsRepository,
 *      onBack: () -> Unit
 *  ) {
 *      val postsState = fetchPost(postId, postsRepository)
 *      if (postsState is UiState.Success<Post>) {
 *           ArticleScreen(postsState.data,onBack)
 *      }
 * }
 *
 *   。。。。。。
 *
 *
 */


/**
 * 2020/7/3
 *
 * 关于UI类名的命名方式
 *
 * aaaxxxScreen代表这个包的入口类，aaa可以让这个类一直放置在包的顶部
 *
 * In00xxx,In01xxx,In03xxx,,,,代表同一条枝干，
 *
 *                           Hu 2  6
 *                          /   |   \
 *                        /     |     \
 *                      /       |       \
 *    枝干源头首单词首两个字母      |      同一枝干的枝节层次（步骤1，2，3，4，，）
 *                              |
 *                          枝干上的枝干
 *                       枝干上的枝干的枝干。。
 *
 * 一个UI功能模块包是这样组成的
 *                              ___这里的情况是在枝干的枝干上又长出新枝干，它们共同放置在同一个包中
 *                 Au04...    /    第一个数字由 0 --> 1 代表了枝干上长出的枝干
 *              Au03Chat    /
 *            Au02...  \  /     CH12...
 *         Au01...      \    CH11...               An01...
 *      Au00Au...        \Ch10...               An00...
 * ========================================================【枝干】
 *              Ui00Ui...             En00...
 *                 Ui01...               En01...
 *                   Ui02...              En02....
 *                     Ui03...
 *
 *  所有类尽量平铺在一条枝干上，当类的数量多到一定程度的时候再将它们包装成包
 *
 *  所做的这一切都是为了减少包的层级，层级的减少可以带来很多好处：
 *
 *  1，减少鼠标的点击，可以尽快了解一个功能模块都做了些什么事情
 *  2，通过给类名增加两个字母和两个数字，就给代码结构赋予了脑图一样的功能，
 *    自己可以根据这个“脑图”，快速找到自己需要查找的代码，别人看你的代码时，
 *    也可以通过“脑图”和后面相关单词，快速联想到该模块都做了些什么事情，
 *    与及如何去做这些事情。
 *  3，【@composable】函数的状态值是层层传递的，想知道它的源头在哪里并不容易
 *    有了“脑图”我们就可以快速对它进行定位。
 *
 *
 *  2020/7/6
 *
 *  类名不带数字，也不带Screen，纯粹是Compose组件的类，相当于枝干的叶子
 *  把它们单独出来,是因为放在一个类之内的话，内容太多，查找困难。
 *  Compose组件的好处就在于它可以随意放置,没有太多的条条框框
 *
 *  video
 *      aaaVideoListScreen.kt   ------ 枝结
 *      Ma01Matrix.kt         \
 *      Yo00Youtube.kt         } 枝干
 *      Bi00BilibiliHome.kt  /
 *      Adfskd.kt                ---
 *      PostCards.kt                |
 *      PostCardTops.kt              } 叶子
 *      PostCardYourNetwork.kt      |
 *      Swipe.kt                 ---
 *
 *
 * 2020/7/7
 *
 * 用 xxx69xxx 代表枝结（枝干与枝干连结的地方）
 *
 * 比如  AddressBook69Gallery
 *
 * xxx69xxx这样的类是给Router使用的，Router格式基本固定了，所以基本不用写注释
 *
 * 这样的特点真的与“枝干”很像，哈。。。
 *
 */
