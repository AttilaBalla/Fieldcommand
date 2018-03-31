from flask import Flask, render_template, redirect, request, session, jsonify, g
import os
# import user
# import dbhandler
import json
from datetime import datetime


app = Flask(__name__)
app.secret_key = 'You would never guess it!'


def extract_form():
    form_input = request.form
    form_dict = {}
    for item in form_input.items():
        form_dict[item[0]] = item[1]

    return form_dict

'''
@app.before_request
def set_current_user():
    try:
        g.current_user = user.get_user_from_db('id', session['user_id'])
    except KeyError:
        g.current_user = None
'''


@app.route('/')
def route_index():
    return render_template('index.html')


@app.route('/about')
def route_about():
    return render_template('about.html')


@app.route('/admin')
def route_admin():
    return render_template('administration.html')

'''
@app.route('/login')
def route_login():
    return render_template('login.html', login_active='class=active')
'''

'''
@app.route('/login-user', methods=['POST'])
def route_login_user():
    login_data = extract_form()
    login = user.verify_login(login_data)
    if (not login):
        return render_template('login.html', error='The given credentials are invalid!')
    else:
        session['user_id'] = login
        return redirect('/')
'''


@app.route("/logout-user")
def route_logout():
    session.clear()
    return redirect('/')


if __name__ == '__main__':
    app.run(
        debug=True  # Allow verbose error reports
    )
