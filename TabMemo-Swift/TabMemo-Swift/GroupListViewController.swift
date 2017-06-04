//
//  GroupListViewController.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 5. 27..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import UIKit
import EZAlertController
import RealmSwift

class GroupListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var groupTableView: UITableView!
    
    let realm = try! Realm()
    var group: Group!
    var notificationToken: NotificationToken?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        groupTableView.rowHeight = UITableViewAutomaticDimension
        groupTableView.estimatedRowHeight = 140
        groupTableView.delegate = self
        groupTableView.dataSource = self
        
        // Set results notification block
        self.notificationToken = group.groupMemos.addNotificationBlock { (changes: RealmCollectionChange) in
            switch changes {
            case .initial:
                // Results are now populated and can be accessed without blocking the UI
                self.groupTableView.reloadData()
                break
            case .update(_, let deletions, let insertions, let modifications):
                // Query results have changed, so apply them to the TableView
                self.groupTableView.beginUpdates()
                self.groupTableView.insertRows(at: insertions.map { IndexPath(row: $0, section: 0) }, with: .automatic)
                self.groupTableView.deleteRows(at: deletions.map { IndexPath(row: $0, section: 0) }, with: .automatic)
                self.groupTableView.reloadRows(at: modifications.map { IndexPath(row: $0, section: 0) }, with: .automatic)
                self.groupTableView.endUpdates()
                break
            case .error(let err):
                // An error occurred while opening the Realm file on the background worker thread
                fatalError("\(err)")
                break
            }
        }
    }

    func selectAddMode() {
        EZAlertController.alert("선택", message: "Which do you want add item", buttons: ["Memo", "Group"]) { (alertAction, position) -> Void in
            if position == 0 {
                self.selectAddMemo()
            } else if position == 1 {
                self.selectAddGroup()
            }
        }
    }
    
    func selectAddGroup() {
        self.performSegue(withIdentifier: "SegAddGroup", sender: self)
    }
    
    func selectAddMemo() {
        self.performSegue(withIdentifier: "SegAddMemo", sender: self)
    }
    
    func setAddGroup(_ segue: UIStoryboardSegue, _ sender: Any?) {
        if let groupView = segue.destination as? GroupViewController {
            let realm = try! Realm()
            try! realm.write {
                let group = realm.create(Group.self)
                group.createUID("id")
                group.parentGroupId = self.group.id
                group.isValidated = false
                group.last_date = String(Date().timeIntervalSince1970)
                
                let groupMemo = realm.create(GroupMemo.self)
                groupMemo.createUID("id")
                groupMemo.parentGroupId = self.group.id
                groupMemo.group = group
                groupMemo.type_id = group.id
                groupMemo.type = GroupMemo.type_group
                groupMemo.isValidated = false
                groupMemo.last_date = String(Date().timeIntervalSince1970)
                
                groupView.group = group
                
                self.group.groupMemos.append(groupMemo)
            }
        }
    }
    
    func setEditGroup(_ segue: UIStoryboardSegue, _ sender: Any?) {
        if let groupView = segue.destination as? GroupViewController {
            let realm = try! Realm()
            let index = groupTableView.indexPath(for: sender as! UITableViewCell)
            let group = self.group.groupMemos[(index?.row)!].group!
            try! realm.write {
                group.isValidated = false
                group.last_date = String(Date().timeIntervalSince1970)
            }
            groupView.group = group
        }
    }
    
    func setAddMemo(_ segue: UIStoryboardSegue, _ sender: Any?) {
        if let memoView = segue.destination as? MemoViewController {
            let realm = try! Realm()
            try! realm.write {
                let memo = realm.create(Memo.self)
                memo.createUID("id")
                memo.parentGroupId = self.group.id
                memo.isValidated = false
                memo.last_date = String(Date().timeIntervalSince1970)
                
                let groupMemo = realm.create(GroupMemo.self)
                groupMemo.createUID("id")
                groupMemo.parentGroupId = self.group.id
                groupMemo.memo = memo
                groupMemo.type_id = memo.id
                groupMemo.type = GroupMemo.type_memo
                groupMemo.isValidated = false
                groupMemo.last_date = String(Date().timeIntervalSince1970)
                
                memoView.memo = memo
                
                self.group.groupMemos.append(groupMemo)
            }
        }
    }
    
    func setEditMemo(_ segue: UIStoryboardSegue, _ sender: Any?) {
        if let memoView = segue.destination as? MemoViewController {
            let realm = try! Realm()
            let index = groupTableView.indexPath(for: sender as! GroupCell)
            let memo = self.group.groupMemos[(index?.row)!].memo!
            try! realm.write {
                memo.isValidated = false
                memo.last_date = String(Date().timeIntervalSince1970)
            }
            memoView.memo = memo
        }
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "SegAddGroup" {
            setAddGroup(segue, sender)
        } else if segue.identifier == "SegEditGroup" {
            setEditGroup(segue, sender)
        } else if segue.identifier == "SegAddMemo" {
            setAddMemo(segue, sender)
        } else if segue.identifier == "SegEditMemo" {
            setEditMemo(segue, sender)
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let groupMemos = group?.groupMemos {
            return groupMemos.count
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let groupMemo: GroupMemo = self.group.groupMemos[indexPath.row]
        if groupMemo.type == GroupMemo.type_group {
            let group: Group = groupMemo.group!
            let groupCell: GroupCell = tableView.dequeueReusableCell(withIdentifier: "GroupCell", for: indexPath) as! GroupCell
            
            groupCell.titleLabel.text = group.title
            groupCell.countLabel.text = String(group.groupMemos.count)
            groupCell.iconImage.image = #imageLiteral(resourceName: "folder")
            if group.tags.count > 0 {
                groupCell.tagListView.isHidden = false
                groupCell.tagListView.removeAllTags()
                for tag in group.tags {
                    groupCell.tagListView.addTag(tag.text)
                }
            } else {
                groupCell.tagListView.isHidden = true
            }
            return groupCell
        } else if groupMemo.type == GroupMemo.type_memo {
            let memo: Memo = groupMemo.memo!
            let memoCell: MemoCell = tableView.dequeueReusableCell(withIdentifier: "MemoCell", for: indexPath) as! MemoCell
            
            memoCell.titleLabel.text = memo.title
            memoCell.iconImage.image = #imageLiteral(resourceName: "document")
            if memo.tags.count > 0 {
                memoCell.tagListView.isHidden = false
                memoCell.tagListView.removeAllTags()
                for tag in memo.tags {
                    memoCell.tagListView.addTag(tag.text)
                }
            } else {
                memoCell.tagListView.isHidden = true
            }
            return memoCell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if group.groupMemos[indexPath.row].type == GroupMemo.type_group {
            self.performSegue(withIdentifier: "SegEditGroup", sender: self)
        } else if group.groupMemos[indexPath.row].type == GroupMemo.type_memo {
            self.performSegue(withIdentifier: "SegEditMemo", sender: self)
        }
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        var result: [UITableViewRowAction] = []
        
        let delete: UITableViewRowAction = UITableViewRowAction(style: .normal, title: "Delete") { (action, index) -> Void in
            tableView.isEditing = false
            try! self.realm.write {
                self.realm.delete(self.group.groupMemos[index.row])
            }
        }
        delete.backgroundColor = UIColor.red
        result.append(delete)
        
        if group.groupMemos[indexPath.row].type == GroupMemo.type_group {
            let edit: UITableViewRowAction = UITableViewRowAction(style: .normal, title: "Edit") { (action, index) -> Void in
                tableView.isEditing = false
                self.performSegue(withIdentifier: "SegEditGroup", sender: self)
            }
            edit.backgroundColor = UIColor.gray
            result.append(edit)
        }
        
        return result
    }
    
    @IBAction func addBtnAction(_ sender: UIBarButtonItem) {
        selectAddMode()
    }
}
