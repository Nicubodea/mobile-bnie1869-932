import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, TouchableHighlight} from 'react-native';
import Communications from 'react-native-communications';

class ViewList extends React.Component {
    static navigationOptions = {
        tabBarLabel: 'ViewList',
    };
    constructor(props) {
        super(props);
        // force re-render when data-set is changed.
        const data_set = new ListView.DataSource({rowHasChanged: (r1, r2) => true});
        global.vieewlist = this;
        global.rentbikeplaces = [];
        global.sync.getAll();
        this.state = { dataSource: data_set.cloneWithRows(global.rentbikeplaces), };

    }


    update_callback()
    {
        this.setState({dataSource: this.state.dataSource.cloneWithRows(global.rentbikeplaces)})
    }



    do_edit(row_data) {
        this.props.navigation.navigate("ViewElement", {data:row_data});
    }

    do_create() {
        this.props.navigation.navigate("AddElement");
    }

    render_row(row_data) {
        if(row_data.state === "deleted")
        {
            return null;
        }
        return(
            <TouchableHighlight onPress={()=> this.do_edit(row_data)}>
                <Text>
                    {row_data.street + ": " + row_data.numberOfAvailable +"/"+row_data.numberOfBikes}
                </Text>
            </TouchableHighlight>
        );
    }

    render() {
        return (
            <View>
                <Button title={"Create"} onPress={() => this.do_create()}/>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={(rowData)=>this.render_row(rowData)}
                />
            </View>

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

export default ViewList;