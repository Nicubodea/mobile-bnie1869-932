import {React} from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView} from 'react-native';
import {Communications} from 'react-native-communications';
import {TabNavigator, NavigationActions, StackNavigator} from 'react-navigation';

global.rentbikeplaces = [
    {
        street:"Strada Fabricii nr. 15",
        numberOfBikes:33,
        numberOfAvailable:20
    },
    {
        street:"Strada 21 Decembrie nr. 15",
        numberOfBikes:55,
        numberOfAvailable:13
    },
    {
        street:"Strada Alunelor nr. 44",
        numberOfBikes:16,
        numberOfAvailable:9
    },
]

class Contact extends React.Component {
    static navigationOptions = {
        tabBarLabel: 'Contact',

    };


    constructor(props) {
        super(props);
        this.state = {subject:"", body:""}
    }

    send_mail() {
        Communications.email(["nicubodea96@gmail.com"],null, null, this.state.subject, this.state.body);

    }

    render() {
        return (
            <ScrollView style={styles.container}>
              <Text style={styles.titleText}>
                Contact
              </Text>
              <Text>
                Subject
              </Text>
              <TextInput style={styles.defaultTextInput} value={this.state.subject} onChangeText={(subject)=>this.setState({subject:subject, body:this.state.body})}>
              </TextInput>
              <Text>
                Message
              </Text>
              <TextInput style={styles.defaultMultiLine} multiline={true} value={this.state.body} onChangeText={(body)=>this.setState({subject:this.state.subject, body:body})}>
              </TextInput>
              <Button style={styles.defaultButton} title="Send" onPress={this.send_mail.bind(this)}>
              </Button>
            </ScrollView>
        );
    }
}


class ViewList extends React.Component {
    static navigationOptions = {
        tabBarLabel: 'ViewList',
    };
    constructor(props) {
        super(props);
        const data_set = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = { dataSource: data_set.cloneWithRows(global.rentbikeplaces), };
    }

    render_row(row_data) {
        return(
            <Button title={row_data.street} onPress={()=> this.props.navigation.navigate("ViewElement", row_data)}/>
        );
    }

    render() {
        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={(rowData)=>this.render_row(rowData)}
            />

        );
    }


}

class ViewElement extends React.Component {
    static navigationOptions = {
    };
    constructor(props) {
        super(props);
        this.state = {street: "", numberOfBikes:0, numberOfAvailable:0}

        var current_element = this.props.navigation.state.params;


        this.state.street = current_element.street;
        this.state.numberOfBikes = current_element.numberOfBikes;
        this.state.numberOfAvailable = current_element.numberOfAvailable;
    }

    edit() {
        if(isNaN(this.state.numberOfBikes) || isNaN(this.state.numberOfAvailable))
        {
            alert("Invalid input numbers!");
            return;
        }
        if(this.state.numberOfAvailable > this.state.numberOfBikes)
        {
            alert("Number of available bikes can't be higher than number of bikes");
            return;
        }

        rentbikeplace = this.state;
        for(var i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)==0){
                global.rentbikeplaces[i].numberOfBikes = rentbikeplace.numberOfBikes;
                global.rentbikeplaces[i].numberOfAvailable = rentbikeplace.numberOfAvailable;
            }
        }
        this.props.navigation.goBack();
    }

    render() {
        return (
            <ScrollView style={styles.container}>
              <Text style={styles.titleText}>{this.state.street}</Text>
              <TextInput style={styles.defaultTextInput} value={this.state.numberOfBikes.toString()} onChangeText={(numberOfBikes)=>this.setState({numberOfBikes})}/>
              <TextInput style={styles.defaultTextInput} value={this.state.numberOfAvailable.toString()} onChangeText={(numberOfAvailable)=>this.setState({numberOfAvailable})}/>
              <Button title="Edit" onPress={()=>this.edit()}/>
            </ScrollView>
        );
    }


}

const styles = StyleSheet.create({
    navbar: {
        flex:1,
        flexDirection:"row",
        justifyContent: "space-around"
    },
    container: {
        paddingVertical: 60
    },
    titleText: {
        fontSize:48
    },
    defaultTextInput: {
        height:40
    },
    defaultMultiLine: {
        height: 250,
        textAlignVertical: 'top'
    },
    defaultButtonNav: {

    }
});

const RentBikeApp = TabNavigator({
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

const MainScreenNavigator = StackNavigator({
    Home: { screen: RentBikeApp },
    ViewElement: {
        screen: ViewElement,
        path: "ViewElement/:rentbikeplace"
    }
});



export default MainScreenNavigator;
