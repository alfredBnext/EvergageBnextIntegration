import Foundation

class ListSaleLine: Decodable{
    var list: [SaleLine]
    
    init(list: [SaleLine]){
        self.list = list;
    }
}
