import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, AppRegistry} from 'react-native';
import Communications from 'react-native-communications';
import {TabNavigator, NavigationActions, StackNavigator} from 'react-navigation';

import Contact from './Modules/common/Contact';
import ViewElement from './Modules/admin/ViewElement';
import ViewList from './Modules/admin/ViewList';
import AddElement from './Modules/admin/AddElement';
import Login from './Modules/common/Login';
import UserViewElement from './Modules/user/UserViewElement';
import UserViewList from './Modules/user/UserViewList';


import {LocalStorage} from './Modules/LocalStorageCalls';


global.rentbikeplaces = [

];

global.token = "";

LocalStorage.getToken("token").then(() => console.log("my token is " + global.token));

// we assume we are in online state until api fails
global.devicestate = "online";

const AdminTabNavigator = TabNavigator({
	Home: {
		screen: Contact,
	},
	ViewList: {
		screen: ViewList
	},
	
},
{
  tabBarPosition: 'top',
  animationEnabled: true,
});

const AdminNavigator = StackNavigator({
    Home: { screen: AdminTabNavigator },
    ViewElement: {
        screen: ViewElement,
        path: "ViewElement/:rentbikeplace"
    },
    AddElement: {
        screen: AddElement
    }
});

const UserTabNavigator = TabNavigator({
        Home: {
            screen: Contact,
        },
        ViewList: {
            screen: UserViewList
        },

    },
    {
        tabBarPosition: 'top',
        animationEnabled: true,
    });

const UserNavigator = StackNavigator({
   Home: { screen: UserTabNavigator},
   ViewElement: {
       screen: UserViewElement,
       path: "UserViewElement/:rentbikeplace"
   }
});

const MainScreenNavigator = StackNavigator({
	Home: { screen: Login },
    AdminPage: { screen: AdminNavigator},
    UserPage: {screen: UserNavigator}
});


global.sync = LocalStorage;

AppRegistry.registerComponent('MyNav', ()=>MainScreenNavigator);

export default MainScreenNavigator;


