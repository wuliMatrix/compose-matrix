package love.matrix.matrix.data

data class Contact( //联系人
    /*var id: String,
    var name: String,
    var email: String,
    var phone: String,
    var image: String,
    var title: String*/
    var author :  String,
    var link : String
)

data class ContactList(var data: List<Contact>)