import React from 'react';
import { AsyncStorage } from 'react-native';

export class LocalStorage {

    static async editOne(id, object)
    {
        console.log(`edit(${id}, ${object}) on async storage called!`);
        AsyncStorage.setItem(id.toString(), JSON.stringify(object), (error) => LocalStorage.handleError(error));
    }

    static async handleError(error: ?Error)
    {
        if(error !== null)
        {
            console.log("[ERROR] encountered: ", JSON.stringify(error));
            return 1;
        }
        console.log("[INFO] Success!");
        return 0;
    }

    static async getToken(key) {
        return AsyncStorage.getItem(key, (error, result) => global.token = result)
    }

    static async setToken(key, val) {
        return AsyncStorage.setItem(key, val, (error) => LocalStorage.handleError(error));
    }

    static async getAll()
    {
        console.log("Get all on async storage called!");
        global.is_list_loaded = false;
        await AsyncStorage.getAllKeys((error, keys) => LocalStorage.keysReady(error, keys));
        console.log(JSON.stringify(global.rentbikeplaces));
    }

    static async keysReady(error, keys)
    {
        global.noKeys = keys.length - 1;
        global.noTotalKeys = 0;
        for (let i = 0; i < keys.length; i++) {
            if(keys[i].localeCompare("token") === 0)
            {
                continue;
            }
            AsyncStorage.getItem(keys[i], (error, result) => LocalStorage.getOne(error, result));
        }
    }

    static async getOne(error, result)
    {
        global.noTotalKeys += 1;
        global.rentbikeplaces.push(JSON.parse(result));
        console.log(`[INFO] Succesfully loaded element ${result}`);
        global.vieewlist.update_callback();

        if(global.noTotalKeys === global.noKeys) {
            global.is_list_loaded = true;
            global.sync_controller.merge();
        }

    }


    static async addOne(id, object)
    {
        console.log(`add(${id}, ${object}) on async storage called!`);
        await AsyncStorage.setItem(id.toString(), JSON.stringify(object), (error) => LocalStorage.handleError(error));

    }

    static async removeOne(id)
    {
        console.log(`delete(${id}) on async storage called!`);
        AsyncStorage.removeItem(id.toString(), (error) => LocalStorage.handleError(error));
    }

    static async printKeys(error: ?Error, keys: ?Array<string>)
    {
        console.log(JSON.stringify(error));
        let i = 0;
        //console.log(JSON.stringify(keys));
        //console.log(keys.length);
        for(i=0; i<keys.length; i++)
        {
            AsyncStorage.getItem(keys[i], (error, result) => LocalStorage.printElement(error, result));
        }
    }

    static async printElement(error: ?Error, result: ?string)
    {
        console.log(JSON.stringify(error));
        console.log(result);
    }

}