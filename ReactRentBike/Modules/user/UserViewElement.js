import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, Alert, Picker, TouchableHighlight} from 'react-native';
import {Pie} from 'react-native-pathjs-charts';

class UserViewElement extends React.Component {
    static navigationOptions = {
    };
    constructor(props) {
        super(props);
        this.state = {street: "", numberOfBikes: 0, numberOfAvailable: 0, active: "Active", isRented: "false"};

        let current_element = this.props.navigation.state.params["data"];
        this.state.street = current_element.street;
        this.state.numberOfBikes = current_element.numberOfBikes;
        this.state.numberOfAvailable = current_element.numberOfAvailable;
        this.state.active = current_element.active;
        if (current_element.isRented) {
            this.state.isRented = current_element.isRented;

        }
    }

    rent() {
        this.setState({isRented:"true"});
        let rentbikeplace = this.state;

        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)===0){
                global.rentbikeplaces[i].numberOfBikes = rentbikeplace.numberOfBikes;
                global.rentbikeplaces[i].numberOfAvailable = rentbikeplace.numberOfAvailable;
                global.rentbikeplaces[i].active = rentbikeplace.active;
                global.rentbikeplaces[i].state = "rented";
                global.rentbikeplaces[i].isRented = "true";
            }
        }
        global.sync.editOne(rentbikeplace.street, rentbikeplace);
    }

    unrent() {
        this.setState({isRented:"false"});
        let rentbikeplace = this.state;

        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)===0){
                global.rentbikeplaces[i].numberOfBikes = rentbikeplace.numberOfBikes;
                global.rentbikeplaces[i].numberOfAvailable = rentbikeplace.numberOfAvailable;
                global.rentbikeplaces[i].active = rentbikeplace.active;
                global.rentbikeplaces[i].state = "unrented";
                global.rentbikeplaces[i].isRented = "false";
            }
        }
        // call api most probably
        global.sync.editOne(rentbikeplace.street, rentbikeplace);
    }

    render() {
        console.log(this.state.numberOfAvailable);
        console.log(this.state.numberOfBikes);
        const data = [
            {
                number:Number(this.state.numberOfBikes-this.state.numberOfAvailable+0.00005)

            },
            {
                number:Number(this.state.numberOfAvailable)
            }
        ];
        const sliceColor = ['#099A0F', '#990000'];

        let options = {
            margin: {
                top: 20,
                left: 20,
                right: 20,
                bottom: 20
            },
            width: 350,
            height: 350,
            color: '#000000',
            r: 50,
            R: 150,
            legendPosition: 'topLeft',
            animate: {
                type: 'oneByOne',
                duration: 200,
                fillTransition: 3
            },
            label: {
                fontFamily: 'Arial',
                fontSize: 8,
                fontWeight: true,
                color: '#ECF0F1'
            }
        };
        let button = null;
        if(this.state.isRented.localeCompare("false") === 0)
        {
            button = <Button title={"Rent"} onPress={()=>this.rent()} raised={true}/>
        }
        else
        {
            button = <Button title={"Unrent"} onPress={()=>this.unrent()} raised={true}/>
        }

        return (
            <ScrollView style={styles.container}>
                <Text style={styles.titleText}>{this.state.street}</Text>
                <Text>Number of bikes: {this.state.numberOfBikes.toString()} </Text>

                <Text>Number of available bikes: {this.state.numberOfAvailable.toString()} </Text>

                <Text> State: {this.state.active} </Text>


                {button}

                <Pie
                    data={data}
                    accessorKey="number"
                    options={options}
                    pallete={
                        [
                            {'r':178,'g':34,'b':34},
                            {'r':34,'g':139,'b':34},
                        ]
                    }
                />


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

    },

    redButton: {
        backgroundColor: "#990000"
    }

});

export default UserViewElement;