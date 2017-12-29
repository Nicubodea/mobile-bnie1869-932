import sqlite3

class DAO:

    class __DAO:
        def __init__(self):
            conn = sqlite3.connect("rentbike.db")

            try:
                conn.execute('''CREATE TABLE USERS
                         (ID INTEGER PRIMARY KEY    AUTOINCREMENT,
                         NAME           TEXT    NOT NULL UNIQUE,
                        PASSHASH        INT     NOT NULL,
                         ROLE           INT     NOT NULL);''')
            except Exception as e:
                print(e)

            try:
                conn.execute('''CREATE TABLE RENTBIKE
                         (ID INTEGER PRIMARY KEY     AUTOINCREMENT,
                         STREET           TEXT    NOT NULL UNIQUE,
                         TOTAL        INT     NOT NULL,
                         AVAILABLE           INT     NOT NULL,
                         ACTIVE               BOOLEAN NOT NULL);''')
            except Exception as e:
                print(e)

            try:
                conn.execute('''CREATE TABLE RENTALS
                         (ID INTEGER PRIMARY KEY     AUTOINCREMENT,
                         ID_USER INT NOT NULL,
                         ID_RENTBIKE INT NOT NULL);''')
            except Exception as e:
                print(e)

            conn.close()

    instance = None
    def __init__(self):
        if DAO.instance is None:
            DAO.instance = DAO.__DAO()

    ###################################################################################################################
    ############################################ RENT BIKE CRUD #######################################################
    ###################################################################################################################

    def add_rent_bike(self, street, total, available, active):
        ans = True
        print("---> add_rent_bike called with params %s %s %s %s" % (street, str(total), str(available), str(active)))
        conn = sqlite3.connect("rentbike.db")
        if active:
            t_act = 1
        else:
            t_act = 0
        try:
            conn.execute("INSERT INTO RENTBIKE (STREET, TOTAL, AVAILABLE, ACTIVE) \
                          VALUES (" + '\'' + street + '\'' + "," + str(total) + "," + str(available) + "," + str(
                t_act) + " )")
            conn.commit()
            print("---> add_rent_bike succeeded")
        except Exception as e:
            print("---> add_rent_bike failed with exception %s, will rollback" % (str(e)))
            conn.rollback()
            ans = False

        conn.close()
        return ans


    def get_all_rent_bike(self):
        print("---> get_all_rent_bike called")
        to_return = []
        conn = sqlite3.connect("rentbike.db")
        try:
            cursor = conn.execute("SELECT * FROM RENTBIKE")
            for row in cursor:
                to_return.append(row)
            conn.close()

            print("---> get_all_rent_bike succeeded - result: %s" % str(to_return))
            return to_return
        except Exception as e:
            print("---> get_all_rent_bike failed")
            return None

    def delete_rent_bike(self, id):
        pass

    def edit_rent_bike(self, id, street, total, available, active):
        pass

    ###################################################################################################################
    ############################################### USERS CRUD ########################################################
    ###################################################################################################################

    def _convert_user_to_dict(self, user):
        dicti = {}
        dicti['id'] = user[0]
        dicti['name'] = user[1]
        dicti['passhash'] = user[2]
        dicti['role'] = user[3]
        return dicti

    def add_user(self, name, pass_hash, role):
        ans = True
        print("---> add_user called with params %s %s %s" % (name, pass_hash, str(role)))
        conn = sqlite3.connect("rentbike.db")
        try:
            conn.execute("INSERT INTO USERS (NAME, PASSHASH, ROLE) \
                                  VALUES (" + '\'' + name + '\'' + "," + '\'' + pass_hash + '\'' + "," + str(role) + " )")
            conn.commit()
            print("---> add_user succeeded")
        except Exception as e:
            print("---> add_user failed with exception %s, will rollback" % (str(e)))
            conn.rollback()
            ans = False

        conn.close()
        return ans

    def get_user_by_name(self, name):
        print("---> get_user_by_name called with param %s" % name)
        to_return = None
        conn = sqlite3.connect("rentbike.db")
        try:
            cursor = conn.execute("SELECT * FROM USERS WHERE NAME = " + '\'' + name + '\'')
            for row in cursor:
                to_return = self._convert_user_to_dict(row)
            conn.close()

            print("---> get_user_by_name succeeded - result: %s" % str(to_return))
            return to_return
        except Exception as e:
            print("---> get_user_by_name failed: %s" % str(e))
            return None

    ###################################################################################################################
    ############################################### RENTALS CRUD ######################################################
    ###################################################################################################################

    def add_rental(self, id_user, id_rentbike):
        pass

    def delete_rental(self, id_user, id_rentbike):
        pass

    def get_all_rentals(self, id_user):
        pass


if __name__ == "__main__":
    dao = DAO()
    dao.add_rent_bike("test", 1, 2, False)
    dao2 = DAO()
    dao2.get_all_rent_bike()
