<template>
  <div class="d-flex justify-content-center">
    <form @submit.prevent="submitted"
      class="w-100 row justify-content-center align-self-center needs-validation">
      <div class="col-md-5 m-0">
      <transition-group name="register">
        <div class="form-group" key="username">
          <label for="input-username" class="">Username</label>
          <input type="text"
            class="form-control"
            name="input-username"
            id="input-username"
            v-model="username">
        </div>
        <div class="form-group" key="password">
          <label for="input-password" class="">Password</label>
          <input type="password"
            name="input-password"
            id="input-password"
            class="form-control"
            v-model="password">
        </div>
        <div class="form-group" v-if="isRegistering" key="verifyPassword">
          <label for="verify-password">Verify Password</label>
          <input type="password"
            name="input-verify-password"
            id="input-verify-password"
            class="form-control"
            :class="{'is-invalid' : !passMatch}"
            v-model="verifyPassword">
          <div class="invalid-feedback">
            Passwords do not match!
          </div>
        </div>
        <div class="form-group" key="isRegistering">
          <div class="form-check">
            <input type="checkbox"
              name="registerCheck"
              id="registerCheck"
              class="form-check-input"
              v-model="isRegistering">
            <label for="registerCheck" class="form-check-label">
              New User?
            </label>
          </div>
        </div>
        <div class="text-center" key="submit">
        <button type="submit"
          class="btn btn-primary mx-auto"
          :disabled="!passMatch">{{ button }}</button>
        </div>
      </transition-group>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      isRegistering: false,
      verifyPassword: '',
    };
  },
  computed: {
    button() {
      return this.isRegistering ? 'Register' : 'Login';
    },
    passMatch() {
      return !this.isRegistering || this.verifyPassword === this.password;
    },
  },
  methods: {
    submitted() {
      const siteUrl = document.URL;

      if (this.isRegistering === true) {
        fetch(new URL('../register', siteUrl), {
          method: 'POST',
          mode: 'cors',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        })
          .then((response) => {
            if (response !== null) {
              return response.json();
            }
            throw new Error('Cannot access server');
          })
          .then((data) => {
            if(data.error !== undefined) throw new Error(data.message);
            this.$store.commit('clearWarning');
            this.isRegistering = false;
          })
          .catch((error) => {
            console.log(error.message);
            this.$store.commit('setWarning',
              'Could not register: ' +
              error.message);
          });
      } else {
        fetch(new URL('../authenticate', siteUrl), {
          method: 'POST',
          mode: 'cors',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        })
          .then((response) => {
            if (response !== null) {
              return response.json();
            }
            throw new Error('Cannot access server');
          })
          .then((data) => {
            if(data.error !== undefined) throw new Error(data.message);
            this.$store.commit('setToken', data.token);
            this.$store.commit('clearWarning');
            this.username = '';
            this.password = '';
            this.verifyPassword = '';
          })
          .catch((error) => {
            console.log(error);
            this.$store.commit('setWarning',
              'Could not log in: ' +
              error);
          });
      }
    },
  },
};
</script>

<style scoped>
.register-move {
  transition: transform 200ms;
}

.register-enter {
  opacity: 0;
}

.register-enter-active {
  transition: opacity 100ms;
}

.register-leave-active {
  opacity: 0;
  transition: opacity 100ms;
  position: absolute;
}
</style>
