//
//  GroupMemo.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 3..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import RealmSwift

class GroupMemo: Object {
    public static let type_group: Int = 1
    public static let type_memo: Int = 2
    
    dynamic var id: String = ""
    
    dynamic var type: Int = 0
    dynamic var parentGroupId: String = ""
    dynamic var type_id: String = "" // TYPE에 맞는 ID(Group 또는 Memo)
    dynamic var group: Group?
    dynamic var memo: Memo?
    
    dynamic var last_date: String = ""
    dynamic var isValidated: Bool = false
}
