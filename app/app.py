from flask import Flask, request, render_template_string, redirect, flash
import hvac
import os

app = Flask(__name__)
app.secret_key = 'supersecret'

VAULT_ADDR = os.environ.get("VAULT_ADDR", "http://vault:8200")
VAULT_TOKEN = os.environ.get("VAULT_TOKEN", "root")

client = hvac.Client(url=VAULT_ADDR, token=VAULT_TOKEN)

TEMPLATE = '''
<!doctype html>
<title>Vault Key Entry</title>
<h2>Добавить секрет в Vault</h2>
<form method=post>
  <label>Ключ:</label><br>
  <input type=text name=key required><br><br>
  <label>Значение:</label><br>
  <input type=text name=value required><br><br>
  <input type=submit value="Сохранить">
</form>
{% with messages = get_flashed_messages() %}
  {% if messages %}
    <ul style="color: green;">
    {% for message in messages %}
      <li>{{ message }}</li>
    {% endfor %}
    </ul>
  {% endif %}
{% endwith %}
'''

@app.route('/', methods=['GET', 'POST'])
def vault_form():
    if request.method == 'POST':
        key = request.form['key']
        value = request.form['value']
        client.secrets.kv.v2.create_or_update_secret(
            path=key,
            secret={'value': value},
            mount_point='secret'
        )
        flash(f"Секрет '{key}' сохранён в Vault.")
        return redirect('/')
    return render_template_string(TEMPLATE)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
