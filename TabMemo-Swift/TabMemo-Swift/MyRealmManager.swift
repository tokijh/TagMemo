//
//  MyRealmManager.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 1..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import RealmSwift

extension Object {
    func createUID(_ forKey: String) -> String {
        let id = (String(Date().timeIntervalSince1970 * 1000) + String.getRandom(6)).sha256
        self.setValue(id, forKey: forKey)
        return id
    }
}
