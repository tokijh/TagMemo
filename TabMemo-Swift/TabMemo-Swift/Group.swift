//
//  Group.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 5. 28..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import RealmSwift

class Group: Object {
    dynamic var id: String = ""
    
    dynamic var parentGroupId: String = ""
    dynamic var title: String = ""
    let tags = List<HashTag>()
    let groupMemos = List<GroupMemo>()
    
    dynamic var last_date: String = ""
    dynamic var isValidated: Bool = false
}
