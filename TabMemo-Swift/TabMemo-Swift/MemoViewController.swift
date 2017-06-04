//
//  MemoViewController.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 1..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import UIKit
import TagListView
import RealmSwift

class MemoViewController: UIViewController {

    let realm = try! Realm()
    var memo: Memo!
    
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var contentTextField: UITextView!
    @IBOutlet weak var tagTextField: UITextField!
    @IBOutlet weak var tagListView: TagListView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleTextField.text = memo.title
        contentTextField.text = memo.content
        tagListView.setTags(Array(memo.tags))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        if let title = titleTextField.text , let content = contentTextField.text {
            try! realm.write {
                memo.title = title
                memo.content = content
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
                        memo.tags.append(hashTag)
                    }
                }
            }
            tagTextField.text = ""
        }
    }
}
