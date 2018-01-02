import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, AppRegistry, NetInfo, Alert} from 'react-native';
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
import {SyncController} from "./Modules/SyncController";


global.rentbikeplaces = [

];

global.token = null;

// we assume we are in online state until api fails
global.devicestate = "online";

global.sync_controller = new SyncController();

global.isLoggedIn = false;

NetInfo.isConnected.addEventListener(
    'connectionChange',
    global.sync_controller.networkStateHasChanged.bind(global.sync_controller)
);

const AdminTabNavigator = TabNavigator({
	Home: {
		screen: Contact,
	},
	ViewList: {
		screen: ViewList
	},
    Logout: {
        screen: Login,
        navigationOptions: ({navigation}) => ({
        tabBarOnPress: (scene, jumpToIndex) => {
            return Alert.alert(
                'Confirmation required'
                ,'Do you really want to logout?'
                ,[
                    {text: 'Accept', onPress: () => { global.isLoggedIn = false; global.token = null; LocalStorage.removeOne("token").then(() => navigation.dispatch(NavigationActions.navigate({ routeName: 'Login' })))}},
                    {text: 'Cancel'}
                ]
            );
        },
    })
    }
	
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
        Logout: {
            screen: Login,
            navigationOptions: ({navigation}) => ({
                tabBarOnPress: (scene, jumpToIndex) => {
                    return Alert.alert(
                        'Confirmation required'
                        ,'Do you really want to logout?'
                        ,[
                            {text: 'Accept', onPress: () => { global.isLoggedIn = false; global.token = null; LocalStorage.removeOne("token").then(() => navigation.dispatch(NavigationActions.navigate({ routeName: 'Login' })))}},
                            {text: 'Cancel'}
                        ]
                    );
                },
            })
        }

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
	Login: { screen: Login, navigationOptions: ({navigation}) => ({
        header: null
    }),},
    AdminPage: { screen: AdminNavigator, navigationOptions: ({navigation}) => ({
        header: null
    }),},
    UserPage: {screen: UserNavigator, navigationOptions: ({navigation}) => ({
        header: null
    }),},

},
{
    initialRouteName: 'Login'
});


global.sync = LocalStorage;

AppRegistry.registerComponent('MyNav', ()=>MainScreenNavigator);

export default MainScreenNavigator;


