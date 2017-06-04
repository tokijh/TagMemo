//
//  GroupCell.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 5. 27..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import UIKit
import TagListView

class GroupCell: UITableViewCell {
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var countLabel: UILabel!
    @IBOutlet weak var iconImage: UIImageView!
    @IBOutlet weak var tagListView: TagListView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
