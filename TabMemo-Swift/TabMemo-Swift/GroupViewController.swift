//
//  GroupViewController.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 1..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import UIKit
import TagListView
import RealmSwift

class GroupViewController: UIViewController {

    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var tagTextField: UITextField!
    @IBOutlet weak var tagListView: TagListView!
    
    var group: Group!
    let realm = try! Realm()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleTextField.text = group.title
        tagListView.setTags(Array(group.tags))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        if let title = titleTextField.text {
            try! realm.write {
                group.title = title
            }
        }
        super.viewWillDisappear(animated)
    }

    @IBAction func addTagBtnAction(_ sender: UIButton) {
        if let tagname = tagTextField.text {
            let split = tagname.components(separatedBy: " ")
            for tag in split {
                if let hashTag = tagListView.addTag(tag, save: true) {
                    try! realm.write {
                        group.tags.append(hashTag)
                    }
                }
            }
            tagTextField.text = ""
        }
    }
}
