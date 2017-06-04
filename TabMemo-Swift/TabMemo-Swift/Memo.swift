//
//  Memo.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 1..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import RealmSwift

class Memo: Object {
    dynamic var id: String = ""
    
    dynamic var parentGroupId: String = ""
    dynamic var title: String = ""
    dynamic var content: String = ""
    let tags = List<HashTag>()
    
    dynamic var last_date: String = ""
    dynamic var isValidated: Bool = false
}
