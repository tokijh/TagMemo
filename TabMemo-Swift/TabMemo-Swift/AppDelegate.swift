//
//  AppDelegate.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 5. 27..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import UIKit
import RealmSwift

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func initFirstData() {
        let realm = try! Realm()
        if let _ = realm.objects(Group.self).filter({ $0.id == "/" }).first {
        
        } else {
            try! realm.write {
                let group = realm.create(Group.self)
                group.id = "/"
                group.title = "모든 메모"
                group.isValidated = false
                group.last_date = String(Date().timeIntervalSince1970)
                
                let groupMemo = realm.create(GroupMemo.self)
                groupMemo.id = "/"
                groupMemo.parentGroupId = "/"
                groupMemo.group = group
                groupMemo.isValidated = false
                groupMemo.last_date = String(Date().timeIntervalSince1970)
            }
        }
    }

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        initFirstData()
        
        if let navigationController = self.window?.rootViewController as? UINavigationController {
            if let groupListViewController: GroupListViewController = navigationController.viewControllers.first as? GroupListViewController {
                groupListViewController.group = try! Realm().objects(Group.self).filter({ $0.id == "/" }).first
            }
        }
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}

