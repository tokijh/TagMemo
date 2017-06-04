//
//  Tag.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 5. 28..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import RealmSwift
import TagListView

class HashTag: Object {
    dynamic var id: String = ""
    
    dynamic var text: String = ""
    
    dynamic var last_date: String = ""
    dynamic var isValidated: Bool = false
}

extension TagListView {
    
    func addTag(_ title: String, save: Bool) -> HashTag? {
        for tagView in self.tagViews {
            if tagView.currentTitle == title {
                return nil
            }
        }
        let realm = try! Realm()
        var hashTag = realm.objects(HashTag.self).filter({ $0.text == title }).first
        if hashTag == nil {
            try! realm.write {
                hashTag = realm.create(HashTag.self)
                hashTag!.createUID("id")
                hashTag!.text = title
                hashTag!.isValidated = false
                hashTag!.last_date = String(Date().timeIntervalSince1970)
            }
        }
        addTag(hashTag!)
        return hashTag
    }
    
    func addTag(_ hashTag: HashTag) -> TagView {
        return addTag(hashTag.text)
    }
    
    func setTags(_ hashTags: [HashTag]) {
        for tag in hashTags {
            addTag(tag)
        }
    }
}
