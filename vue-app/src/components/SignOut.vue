<template>
  <div class="d-flex flex-column align-items-center mt-3">
    <button class="btn btn-danger"
      @click.prevent="signOut">
      Sign Out
    </button>
  </div>
</template>

<script>
export default {
  methods: {
    signOut() {
      this.$store.commit('signOut');
    },
  },
};
</script>
