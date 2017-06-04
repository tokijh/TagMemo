//
//  Encrypt.swift
//  TabMemo-Swift
//
//  Created by 윤중현 on 2017. 6. 2..
//  Copyright © 2017년 윤중현. All rights reserved.
//

import Foundation

extension String {
    var sha256: String {
        get {
            let data = self.data(using: String.Encoding.utf8)
            let res = NSMutableData(length: Int(CC_SHA256_DIGEST_LENGTH))
            CC_SHA256(((data! as NSData)).bytes, CC_LONG(data!.count), res?.mutableBytes.assumingMemoryBound(to: UInt8.self))
            let hashedString = "\(res!)".replacingOccurrences(of: "", with: "").replacingOccurrences(of: " ", with: "")
            let badchar: CharacterSet = CharacterSet(charactersIn: "\"<\",\">\"")
            let cleanedstring: String = (hashedString.components(separatedBy: badchar) as NSArray).componentsJoined(by: "")
            return cleanedstring
        }
    }
    
    static func getRandom(_ length: Int) -> String {
        let letters : NSString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        let len = UInt32(letters.length)
        
        var randomString = ""
        
        for _ in 0 ..< length {
            let rand = arc4random_uniform(len)
            var nextChar = letters.character(at: Int(rand))
            randomString += NSString(characters: &nextChar, length: 1) as String
        }
        
        return randomString
    }
}
