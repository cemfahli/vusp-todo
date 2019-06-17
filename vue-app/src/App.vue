<template>
  <div id="app" class="d-flex flex-column">
    <div class="logo container align-items-center">
      <img alt="Vusp logo"
        src="./assets/vusp-logo.svg"
        class="col-md-5 col-7 m-5">
    </div>
    <div class="col-md-5 align-self-center"
      v-if="this.$store.getters.isWarning">
      <my-alert></my-alert>
    </div>
    <div class="mt-3 align-content-center">
      <transition name="main" mode="out-in">
        <my-login v-if="!$store.getters.isAuthorized" key="login"></my-login>
        <div v-else key="list">
          <todo-list></todo-list>
          <add-task></add-task>
          <sign-out></sign-out>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import Login from './components/Login.vue';
import TodoList from './components/TodoList.vue';
import AddTask from './components/AddTask.vue';
import Alert from './components/Alert.vue';
import SignOut from './components/SignOut.vue';

export default {
  name: 'app',
  components: {
    'my-alert': Alert,
    'my-login': Login,
    'todo-list': TodoList,
    'add-task': AddTask,
    'sign-out': SignOut,
  },
};
</script>

<style scoped>
.logo {
  text-align: center;
}
#app {
  font-family: 'Comfortaa', 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.main-enter {
  opacity: 0;
}

.main-enter-active {
  transition: opacity 200ms;
}

.main-leave-active {
  opacity: 0;
  transition: opacity 200ms;
}
</style>
